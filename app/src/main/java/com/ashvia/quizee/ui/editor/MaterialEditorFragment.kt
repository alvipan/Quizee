package com.ashvia.quizee.ui.editor

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ashvia.quizee.databinding.FragmentMaterialEditorBinding
import com.ashvia.quizee.model.Material
import com.github.onecode369.wysiwyg.WYSIWYG
import com.github.onecode369.wysiwyg.WYSIWYG.OnTextChangeListener

class MaterialEditorFragment : Fragment() {

    private var _binding: FragmentMaterialEditorBinding? = null
    private val binding get() = _binding!!
    private val model: EditorViewModel by activityViewModels()
    private lateinit var editor: WYSIWYG
    private var material = Material()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMaterialEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMaterialEditor()
    }

    private fun getMaterialEditor() {
        var isBold = false
        var isItalic = false
        var isUnderline = false

        editor = binding.editor
        editor.setEditorFontSize(18)
        editor.setPadding(16,16,16,16)
        editor.setPlaceholder("Konten materi...")
        editor.html = model.question.value?.material?.content
        editor.setOnTextChangeListener(object : OnTextChangeListener {
            override fun onTextChange(text: String?) {
                material.content = text
                model.setMaterial(material)
            }
        })

        binding.materialPrice.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0.toString().let {
                    if (it.isNotEmpty()) {
                        material.price = it.toInt()
                    } else {
                        material.price = 0
                    }
                }
                model.setMaterial(material)
            }
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        binding.btnBold.setOnClickListener {
            editor.focusEditor()
            editor.setBold()
            isBold = when (isBold) {
                true -> false
                else -> true
            }
            changeTint(binding.btnBold, isBold)
        }

        binding.btnItalic.setOnClickListener {
            editor.focusEditor()
            editor.setItalic()
            isItalic = when (isItalic) {
                true -> false
                else -> true
            }
            changeTint(binding.btnItalic, isItalic)
        }

        binding.btnUnderline.setOnClickListener {
            editor.focusEditor()
            editor.setUnderline()
            isUnderline = when (isUnderline) {
                true -> false
                else -> true
            }
            changeTint(binding.btnUnderline, isUnderline)
        }
    }

    private fun changeTint(view: ImageView, status: Boolean) {
        when (status) {
            true -> view.setColorFilter(Color.BLACK)
            else -> view.setColorFilter(Color.parseColor("#bdbdbd"))
        }
    }
}