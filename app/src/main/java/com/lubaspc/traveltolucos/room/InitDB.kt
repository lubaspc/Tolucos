package com.lubaspc.traveltolucos.room

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InitDB {
    companion object {

        @JvmStatic
        fun insertInitialDB(scope: CoroutineScope) {
            scope.launch {
                withContext(Dispatchers.IO) {
//                    DBRoom.db.dbDao().deleteChargePerson()
                    DBRoom.db.dbDao().apply {
                        insertCharge(
                            ChargeDb(
                                13,
                                "Otro",
                                0.0,
                                1,
                                0.0,
                                TypeCharge.GROUP
                            )
                        )
                        insertCharge(
                            ChargeDb(
                                12,
                                "Otro",
                                0.0,
                                1,
                                0.0,
                                TypeCharge.PERSONAL
                            )
                        )
                        insertPerson(PersonDb(1, "Charly", "7228507896"))
                        insertPerson(PersonDb(2, "Maik", "7223731006"))
                        insertPerson(PersonDb(3, "Orihuela", "7224124088"))
                        insertPerson(PersonDb(4, "Mayle", "7226455089"))
                        insertPerson(PersonDb(5, "Nestor", "7228593599"))
                        insertPerson(PersonDb(6, "Lubin", "7225530820"))
                        //deleteChargePerson()
                    }
                }
            }
        }

        @JvmStatic
        suspend fun insertInitialDB(delete: Boolean = false) {
            if (delete) DBRoom.db.dbDao().deleteChargePerson()
            DBRoom.db.dbDao().apply {
                insertCharge(
                    ChargeDb(
                        11,
                        "(E)",
                        50.0,
                        1,
                        50.0,
                        TypeCharge.GROUP
                    )
                )
                insertCharge(
                    ChargeDb(
                        13,
                        "Otro",
                        0.0,
                        1,
                        0.0,
                        TypeCharge.GROUP
                    )
                )
                insertCharge(
                    ChargeDb(
                        12,
                        "Otro",
                        0.0,
                        1,
                        0.0,
                        TypeCharge.PERSONAL
                    )
                )
                insertPerson(PersonDb(1, "Charly", "7228507896"))
                insertPerson(PersonDb(2, "Maik", "7223731006"))
                insertPerson(PersonDb(3, "Orihuela", "7224124088"))
                insertPerson(PersonDb(4, "Mayle", "7226455089"))
                insertPerson(PersonDb(5, "Nestor", "7228593599"))
                insertPerson(PersonDb(6, "Lubin", "7225530820"))
                //deleteChargePerson()
            }
        }

    }
}