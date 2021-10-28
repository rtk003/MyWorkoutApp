/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.myworkoutapp

import androidx.lifecycle.*
import com.app.myworkoutapp.data.ActivityDAO
import com.app.myworkoutapp.data.ActivityMC
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the Inventory repository and an up-to-date list of all items.
 */
class ActivitiesViewModel(private val activityDAO: ActivityDAO) : ViewModel() {

    // Cache all items form the database using LiveData.
    val allItems: LiveData<List<ActivityMC>> = activityDAO.getItems().asLiveData()

    /**
     * Updates an existing Item in the database.
     */

    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }


    /**
     * Launching a new coroutine to update an item in a non-blocking way
     */
    private fun updateItem(activityMC: ActivityMC) {
        viewModelScope.launch {
            activityDAO.update(activityMC)
        }
    }

    /**
     * Inserts the new Item into database.
     */
    fun addNewItem(title: String, place: String, date: String) {
        val newItem = getNewItemEntry(title, place, date)
        insertItem(newItem)
    }

    /**
     * Launching a new coroutine to insert an item in a non-blocking way
     */
    private fun insertItem(activityMC: ActivityMC) {
        viewModelScope.launch {
            activityDAO.insert(activityMC)
        }
    }

    /**
     * Launching a new coroutine to delete an item in a non-blocking way
     */
    fun deleteItem(activityMC: ActivityMC) {
        viewModelScope.launch {
            activityDAO.delete(activityMC)
        }
    }

    /**
     * Retrieve an item from the repository.
     */
    fun retrieveItem(id: Int): LiveData<ActivityMC> {
        return activityDAO.getItem(id).asLiveData()
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    fun isEntryValid(title: String, place: String, date: String): Boolean {
        if (title.isBlank() || place.isBlank() || date.isBlank()) {
            return false
        }
        return true
    }

    /**
     * Returns an instance of the [Item] entity class with the item info entered by the user.
     * This will be used to add a new entry to the Inventory database.
     */
    private fun getNewItemEntry(title: String, place: String, date: String): ActivityMC {
        return ActivityMC(
            title = title,
            place = place,
            date = date
        )
    }

    /**
     * Called to update an existing entry in the Inventory database.
     * Returns an instance of the [Item] entity class with the item info updated by the user.
     */
    private fun getUpdatedItemEntry(
        itemId: Int,
        title: String,
        place: String,
        date: String
    ): ActivityMC {
        return ActivityMC(
            id = itemId,
            title = title,
            place = place,
            date = date
        )
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class ActivityViewModelFactory(private val itemDao: ActivityDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivitiesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActivitiesViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

