package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentScrapBottomDialogBinding
import com.softsquared.niceduck.android.sparky.model.TagRequest
import com.softsquared.niceduck.android.sparky.model.TagsResponse
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel
import com.softsquared.niceduck.android.sparky.viewmodel.ScrapTemplateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MyBottomDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentScrapBottomDialogBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()

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

        mainViewModel.tagColor.observe(viewLifecycleOwner) {
            binding.scrapBottomDialogLLNewTagName.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(
                    it
                )
            )
        }

        mainViewModel.tagLastLoadResponse.observe(this) {
            if (it.code == "0000") {
                it.result.tagResponses?.let { response ->
                    if (response.isNullOrEmpty()) {
                        binding.scrapBottomDialogLL.visibility = GONE
                    }
                }
            }
        }

        mainViewModel.tagLastLoadFailure.observe(viewLifecycleOwner) {
            Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
        }

        mainViewModel.lastTags.observe(viewLifecycleOwner) {
            mainViewModel.tagAddRecyclerviewAdapter.submitList(it)
            setTagAddRecyclerview()
        }

        mainViewModel.hideBottomSheetCall.observe(viewLifecycleOwner) {
            dismiss()
        }


        binding.scrapBottomDialogLLTagAddBtn.setOnClickListener {
            val color = mainViewModel.tagColor.getValue() ?: "#DFDFDF"

            mainViewModel.postTagSave(
                TagRequest(
                    binding.scrapBottomDialogEditTxt.text.toString(),
                    color
                )
            )
        }

        mainViewModel.tagSaveResponse.observe(viewLifecycleOwner) {
            if (it.code == "0000") {

                mainViewModel.scrapTemplateDataSet.value!!.add(TagsResponse(it.result.color, it.result.name, it.result.tagId))

                val newTagList = mainViewModel.scrapTemplateDataSet.value

                mainViewModel.scrapTemplateDataSet.value = newTagList

                dismiss()
            }
        }

        mainViewModel.tagSaveFailure.observe(viewLifecycleOwner) {
            when (it.code) {
                "U000" -> {
                    lifecycleScope.launch {
                        mainViewModel.postReissueAccessToken()
                        val color = mainViewModel.tagColor.getValue() ?: "#DFDFDF"

                        mainViewModel.postTagSave(
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
            if (mainViewModel.updatedList.isNotEmpty()) {
                binding.scrapBottomDialogLL.visibility = GONE
                binding.scrapBottomDialogRecyclerview.visibility = VISIBLE
                binding.scrapBottomDialogTxtLastTagTitle.visibility = VISIBLE
                mainViewModel.tagAddRecyclerviewAdapter.submitList(mainViewModel.updatedList)
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
            mainViewModel.updatedList = mainViewModel.lastTags.value!!.filter { it.name.contains(str) } as ArrayList<TagsResponse>
        else mainViewModel.updatedList = mainViewModel.lastTags.value!!
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
            adapter = mainViewModel.tagAddRecyclerviewAdapter
            visibility = VISIBLE
        }

        if (mainViewModel.lastTags.value == null || mainViewModel.lastTags.value!!.size == 0) {
            binding.scrapBottomDialogTxtLastTagTitle.visibility = GONE
        }
        else {
            binding.scrapBottomDialogTxtLastTagTitle.visibility = VISIBLE

        }
    }
}
