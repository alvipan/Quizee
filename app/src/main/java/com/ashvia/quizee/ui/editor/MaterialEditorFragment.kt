package com.ashvia.quizee.ui.editor

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ashvia.quizee.databinding.FragmentMaterialEditorBinding
import com.ashvia.quizee.data.Material
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
            editor.setBold()
        }

        binding.btnItalic.setOnClickListener {
            editor.setItalic()
        }

        binding.btnUnderline.setOnClickListener {
            editor.setUnderline()
        }

        binding.btnAlignLeft.setOnClickListener {
            editor.setAlignLeft()
        }

        binding.btnAlignRight.setOnClickListener {
            editor.setAlignRight()
        }

        binding.btnAlignCenter.setOnClickListener {
            editor.setAlignCenter()
        }

        binding.btnAlignJustify.setOnClickListener {
            editor.setAlignJustifyFull()
        }

        binding.btnInsertBullets.setOnClickListener {
            editor.setBullets()
        }

        binding.btnInsertNumber.setOnClickListener {
            editor.setNumbers()
        }
    }
}