package com.example.audiodescriptor.data

import kotlinx.serialization.Serializable

@Serializable
data class DummyText(
    val limit: Int? = 0,
    val products: List<Product?>? = listOf(),
    val skip: Int? = 0,
    val total: Int? = 0
) {
    @Serializable
    data class Product(
        val availabilityStatus: String? = "",
        val brand: String? = "",
        val category: String? = "",
        val description: String? = "",
        val dimensions: Dimensions? = Dimensions(),
        val discountPercentage: Double? = 0.0,
        val id: Int? = 0,
        val images: List<String?>? = listOf(),
        val meta: Meta? = Meta(),
        val minimumOrderQuantity: Int? = 0,
        val price: Double? = 0.0,
        val rating: Double? = 0.0,
        val returnPolicy: String? = "",
        val reviews: List<Review?>? = listOf(),
        val shippingInformation: String? = "",
        val sku: String? = "",
        val stock: Int? = 0,
        val tags: List<String?>? = listOf(),
        val thumbnail: String? = "",
        val title: String? = "",
        val warrantyInformation: String? = "",
        val weight: Int? = 0
    ) {
        @Serializable
        data class Dimensions(
            val depth: Double? = 0.0,
            val height: Double? = 0.0,
            val width: Double? = 0.0
        )

        @Serializable
        data class Meta(
            val barcode: String? = "",
            val createdAt: String? = "",
            val qrCode: String? = "",
            val updatedAt: String? = ""
        )

        @Serializable
        data class Review(
            val comment: String? = "",
            val date: String? = "",
            val rating: Int? = 0,
            val reviewerEmail: String? = "",
            val reviewerName: String? = ""
        )
    }
}