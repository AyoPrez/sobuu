package com.ayoprez.sobuu.presentation.shelf

import androidx.lifecycle.ViewModel
import com.ayoprez.sobuu.shared.features.shelf.remote.ShelfRemoteDataImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShelfViewModel @Inject constructor(
    private val shelfRemoteDataImpl: ShelfRemoteDataImpl
): ViewModel() {



}