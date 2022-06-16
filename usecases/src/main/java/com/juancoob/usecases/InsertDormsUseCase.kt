package com.juancoob.usecases

import com.juancoob.data.DormRepository
import com.juancoob.domain.Dorm
import javax.inject.Inject

class InsertDormsUseCase @Inject constructor(private val repository: DormRepository) {
    suspend operator fun invoke(dorms: List<Dorm>) =
        repository.insertDorms(dorms)
}
