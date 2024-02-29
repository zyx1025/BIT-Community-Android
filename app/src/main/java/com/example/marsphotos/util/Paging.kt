package com.example.marsphotos.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.marsphotos.model.moment.Moment

class MomentPagingSource(
    private val apiService: MomentApiService,
    private val token: String
) : PagingSource<Int, Moment>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Moment> {
        try {
            val currentPage = params.key ?: 0
            val response = apiService.getAllMoments("Bearer $token", params.loadSize, currentPage)
            val endOfPaginationReached = response.data.moments.isEmpty()
            return LoadResult.Page(
                data = response.data.moments,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (endOfPaginationReached) null else currentPage + params.loadSize
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Moment>): Int? {
        return state.anchorPosition
    }
}
