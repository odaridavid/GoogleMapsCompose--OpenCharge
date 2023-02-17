package com.github.odaridavid.opencharge.di

import com.github.odaridavid.opencharge.api.PoiRepository
import com.github.odaridavid.opencharge.impl.DefaultPoiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface PoiModule {

    @Binds
    fun bindPoiRepository(weatherRepository: DefaultPoiRepository): PoiRepository

}
