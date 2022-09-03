package com.ashvia.quizee.ui.answer

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import com.ashvia.quizee.MainActivity
import com.ashvia.quizee.R
import com.ashvia.quizee.databinding.FragmentMaterialCheckOutDialogBinding
import com.ashvia.quizee.ui.main.MainViewModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MaterialCheckOutDialogFragment : BottomSheetDialogFragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: FragmentMaterialCheckOutDialogBinding

    private val model: MainViewModel by activityViewModels()

    private var rewardedAd: RewardedAd? = null
    private var isRewarded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        database = Firebase.database
        binding = FragmentMaterialCheckOutDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.let {
            val sheet = it as BottomSheetDialog
            sheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        val question = model.question.value
        val material = question?.material
        binding.questionTitle.text = question?.title
        binding.materialPrice.text = String.format("%s Poin",question?.material?.price)

        model.user.observe(viewLifecycleOwner) {
            if (it.point!! < material?.price!!) {
                binding.paymentMethod[0].isEnabled = false
            }
        }

        binding.payButton.setOnClickListener {
            when (binding.paymentMethod.checkedRadioButtonId) {
                R.id.methodMyPoin -> {
                    val user = model.user.value
                    user?.point = user?.point?.minus(material?.price!!)
                    model.user.value = user
                    addMaterial()
                }
                R.id.methodWatchVideo -> {
                    loadRewardedAd()
                }
            }
        }
    }

    private fun addMaterial() {
        val user = model.user.value
        user?.material?.add(model.questionId.value!!)
        model.user.value = user

        val ref = database.getReference("users").child(auth.uid!!)
        ref.setValue(model.user.value).addOnSuccessListener {
            readMaterial()
        }
    }

    private fun readMaterial() {
        this.dismiss()
        (activity as MainActivity).navController.navigate(R.id.navigation_question_material)
    }

    private fun loadRewardedAd() {
        binding.payButton.isEnabled = false
        binding.progressInfo.text = String.format("Memuat video...")
        binding.progressInfo.setTextColor(Color.GRAY)

        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(requireContext(),"ca-app-pub-5368153434110739/7512801859", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                adError.toString().let { Log.d("Rewarded Ad", it) }
                rewardedAd = null
                binding.payButton.isEnabled = true
                binding.progressInfo.text = String.format("Gagal memuat video.")
                binding.progressInfo.setTextColor(Color.RED)
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Log.d("Rewarded Ad", "Ad was loaded.")
                rewardedAd = ad
                binding.payButton.isEnabled = true
                binding.progressInfo.text = ""
                showRewardedAd()
            }
        })
    }

    private fun showRewardedAd() {
        if (rewardedAd != null) {
            rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("AnswerActivity", "Ad was dismissed.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    rewardedAd = null
                    if (isRewarded) {
                        isRewarded = false
                        addMaterial()
                    }
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.d("AnswerActivity", "Ad failed to show.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    rewardedAd = null
                }
            }

            rewardedAd?.show(requireActivity()) {
                isRewarded = true
            }
        }
    }
}