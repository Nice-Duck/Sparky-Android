package com.softsquared.niceduck.android.sparky.view.scrap

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentScrapBottomDialogBinding
import com.softsquared.niceduck.android.sparky.model.TagRequest
import com.softsquared.niceduck.android.sparky.model.TagsResponse
import com.softsquared.niceduck.android.sparky.viewmodel.ScrapTemplateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ScrapBottomDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentScrapBottomDialogBinding? = null
    private val binding get() = _binding!!
    private val scrapTemplateViewModel: ScrapTemplateViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)
        if (bottomSheetDialog is BottomSheetDialog) {
            bottomSheetDialog.behavior.skipCollapsed = true
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScrapBottomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.scrapBottomDialogImgBackBtn.setOnClickListener {
            dismiss()
        }

        scrapTemplateViewModel.tagColor.observe(viewLifecycleOwner) {
            binding.scrapBottomDialogLLNewTagName.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(
                    it
                )
            )
        }

        scrapTemplateViewModel.tagLastLoadResponse.observe(this) {
            if (it.code == "0000") {
                it.result.tagResponses?.let { response ->
                    if (response.isNullOrEmpty()) {
                        binding.scrapBottomDialogLL.visibility = GONE
                    }
                }
            }
        }

        scrapTemplateViewModel.tagLastLoadFailure.observe(viewLifecycleOwner) {
            Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
        }

        scrapTemplateViewModel.lastTags.observe(viewLifecycleOwner) {
            scrapTemplateViewModel.tagAddRecyclerviewAdapter.submitList(it)
            setTagAddRecyclerview()
        }

        scrapTemplateViewModel.hideBottomSheetCall.observe(viewLifecycleOwner) {
            dismiss()
        }


        binding.scrapBottomDialogLLTagAddBtn.setOnClickListener {
            val color = scrapTemplateViewModel.tagColor.getValue() ?: "#DFDFDF"

            scrapTemplateViewModel.postTagSave(
                TagRequest(
                    binding.scrapBottomDialogEditTxt.text.toString(),
                    color
                )
            )
        }

        scrapTemplateViewModel.tagSaveResponse.observe(viewLifecycleOwner) {
            if (it.code == "0000") {
                val i = scrapTemplateViewModel.scrapTemplateDataSet.value!!.size - 1

                scrapTemplateViewModel.scrapTemplateDataSet.value!!.add(
                    i,
                    TagsResponse(it.result.color, it.result.name, it.result.tagId)
                )

                val newTagList = scrapTemplateViewModel.scrapTemplateDataSet.value

                scrapTemplateViewModel.scrapTemplateDataSet.value = newTagList

                dismiss()
            }
        }

        scrapTemplateViewModel.tagSaveFailure.observe(viewLifecycleOwner) {
            when (it.code) {
                "U000" -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        scrapTemplateViewModel.postReissueAccessToken()
                        val color = scrapTemplateViewModel.tagColor.getValue() ?: "#DFDFDF"

                        scrapTemplateViewModel.postTagSave(
                            TagRequest(
                                binding.scrapBottomDialogEditTxt.text.toString(),
                                color
                            )
                        )
                    }
                }
                else -> {
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.scrapBottomDialogImgSearchDeleteBtn.setOnClickListener {
            binding.scrapBottomDialogEditTxt.text.clear()
        }

        binding.scrapBottomDialogEditTxt.addTextChangedListener {
            if (binding.scrapBottomDialogEditTxt.text.isNotEmpty()) {
                binding.scrapBottomDialogImgSearchDeleteBtn.visibility = VISIBLE
                binding.scrapBottomDialogEditTxt.backgroundTintList = ColorStateList.valueOf(
                    Color.parseColor(
                        "#FF000000"
                    )
                )
                binding.scrapBottomDialogEditTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit_txt_inner_search2, 0, 0, 0)
            } else {
                binding.scrapBottomDialogLL.visibility = GONE
                binding.scrapBottomDialogImgSearchDeleteBtn.visibility = GONE
                binding.scrapBottomDialogEditTxt.backgroundTintList = ColorStateList.valueOf(
                    Color.parseColor(
                        "#BEBDBD"
                    )
                )
                binding.scrapBottomDialogEditTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit_txt_inner_search, 0, 0, 0)
            }

            updateList(binding.scrapBottomDialogEditTxt.text.toString())
            if (scrapTemplateViewModel.updatedList.isNotEmpty()) {
                binding.scrapBottomDialogLL.visibility = GONE
                binding.scrapBottomDialogRecyclerview.visibility = VISIBLE
                binding.scrapBottomDialogTxtLastTagTitle.visibility = VISIBLE
                scrapTemplateViewModel.tagAddRecyclerviewAdapter.submitList(scrapTemplateViewModel.updatedList)
            } else {
                if (binding.scrapBottomDialogEditTxt.text.isNotEmpty()) {
                    binding.scrapBottomDialogLL.visibility = VISIBLE
                    binding.scrapBottomDialogTxtNewTagName.text = binding.scrapBottomDialogEditTxt.text.toString()
                    binding.scrapBottomDialogRecyclerview.visibility = GONE
                    binding.scrapBottomDialogTxtLastTagTitle.visibility = GONE
                }
            }


        }
    }

    private fun updateList(str: String) {
        if (str.isNotBlank())
            scrapTemplateViewModel.updatedList = scrapTemplateViewModel.lastTags.value!!.filter { it.name.contains(str) } as ArrayList<TagsResponse>
        else scrapTemplateViewModel.updatedList = scrapTemplateViewModel.lastTags.value!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setTagAddRecyclerview() {
        val layoutManager = FlexboxLayoutManager(activity)
        layoutManager.flexDirection = FlexDirection.ROW
        with(binding.scrapBottomDialogRecyclerview) {
            this.layoutManager = layoutManager
            adapter = scrapTemplateViewModel.tagAddRecyclerviewAdapter
            visibility = View.VISIBLE
        }

        if (scrapTemplateViewModel.lastTags.value == null || scrapTemplateViewModel.lastTags.value!!.size == 0) {
            binding.scrapBottomDialogTxtLastTagTitle.visibility = GONE
            binding.scrapBottomDialogLL.visibility = VISIBLE
        }
        else {
            binding.scrapBottomDialogTxtLastTagTitle.visibility = VISIBLE
            binding.scrapBottomDialogLL.visibility = GONE
        }
    }
}
