package com.lubaspc.traveltolucos.room

import androidx.lifecycle.lifecycleScope
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
                    DBRoom.db.dbDao().deleteChargePerson()
                    DBRoom.db.dbDao().apply {
                        insertCharge(
                            ChargeDb(
                                9,
                                "Gasolina",
                                23.5,
                                15,
                                352.5,
                                TypeCharge.GROUP
                            )
                        )
                        insertCharge(
                            ChargeDb(
                                11,
                                "(E)",
                                12.0,
                                1,
                                12.0,
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
                        //deleteChargePerson()
                    }
                }
            }
        }

    }
}