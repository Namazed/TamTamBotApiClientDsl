package chat.tamtam.botsdk

import chat.tamtam.botsdk.model.prepared.Update
import chat.tamtam.botsdk.model.prepared.UpdatesList

interface UpdatesDelegate {

    suspend fun coordinate(updatesList: UpdatesList)

    suspend fun coordinateParallel(updatesList: UpdatesList)

    suspend fun coordinate(update: Update)

}