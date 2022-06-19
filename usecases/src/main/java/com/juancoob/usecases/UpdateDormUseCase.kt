package com.juancoob.usecases

import com.juancoob.data.DormRepository
import com.juancoob.domain.Dorm
import com.juancoob.domain.ErrorRetrieved
import javax.inject.Inject

class UpdateDormUseCase @Inject constructor(private val repository: DormRepository) {
    suspend operator fun invoke(dorm: Dorm): ErrorRetrieved? =
        repository.updateDorm(dorm)
}
