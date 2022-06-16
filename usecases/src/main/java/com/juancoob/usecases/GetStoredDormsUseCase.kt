package com.juancoob.usecases

import com.juancoob.data.DormRepository
import javax.inject.Inject

class GetStoredDormsUseCase @Inject constructor(private val repository: DormRepository) {
    suspend operator fun invoke(): Int =
        repository.getStoredDorms()
}
