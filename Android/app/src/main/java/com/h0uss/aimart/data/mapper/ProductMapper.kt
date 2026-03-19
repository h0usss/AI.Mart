package com.h0uss.aimart.data.mapper

import com.h0uss.aimart.data.entity.ProductEntity
import com.h0uss.aimart.data.model.UserProductCardData

fun ProductEntity.toUserProductCardData(): UserProductCardData{
    return UserProductCardData(
        id = this.id,
        name = this.name,
        price = this.price,
        imageId = this.imageId,
        description = this.description,
        status = this.productStatus
    )
}