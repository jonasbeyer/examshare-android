package de.twisted.examshare.data.report

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ReportModule {

    @Singleton
    @Provides
    fun provideRestReportDataSource(retrofit: Retrofit): RestReportDataSource =
        retrofit.create(RestReportDataSource::class.java)
}