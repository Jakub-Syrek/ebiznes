package com.example

import com.example.models.Category
import com.example.models.Gadget

val smartphoneCategory = Category("1", "Smartphones")
val laptopCategory = Category("2", "Laptops")
val tabletCategory = Category("3", "Tablets")
val smartwatchCategory = Category("4", "Smartwatches")

val categoryStorage = mutableListOf<Category>(smartphoneCategory, laptopCategory, tabletCategory, smartwatchCategory)

val gadgetStorage = mutableListOf<Gadget>(
    Gadget("1", "iPhone 13", smartphoneCategory),
    Gadget("2", "Samsung Galaxy S22", smartphoneCategory),
    Gadget("3", "Google Pixel 6", smartphoneCategory),
    Gadget("4", "MacBook Pro", laptopCategory),
    Gadget("5", "Dell XPS 15", laptopCategory),
    Gadget("6", "HP Spectre x360", laptopCategory),
    Gadget("7", "iPad Pro", tabletCategory),
    Gadget("8", "Samsung Galaxy Tab S8", tabletCategory),
    Gadget("9", "Microsoft Surface Pro 8", tabletCategory),
    Gadget("10", "Apple Watch Series 7", smartwatchCategory),
    Gadget("11", "Samsung Galaxy Watch 4", smartwatchCategory),
    Gadget("12", "Garmin Venu 2", smartwatchCategory)
)

