package com.ftofs.twant.adapter

import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseViewHolder

class BoundViewHolder<out T : ViewDataBinding> constructor(val binding: T) :
       BaseViewHolder(binding.root)