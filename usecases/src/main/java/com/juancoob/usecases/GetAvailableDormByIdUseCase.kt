package com.juancoob.usecases

import com.juancoob.data.DormRepository
import com.juancoob.domain.Dorm
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAvailableDormByIdUseCase @Inject constructor(private val repository: DormRepository) {
    operator fun invoke(dormId: Int): Flow<Dorm> =
        repository.getAvailableDormById(dormId)
}
