package com.lubaspc.traveltolucos.room

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InitDB {
    companion object{

        @JvmStatic
        fun insertInitialDB(scope: CoroutineScope) {
            scope.launch {
                withContext(Dispatchers.IO) {
                    DBRoom.db.dbDao().apply {
                        insertCharge(
                            ChargeDb(
                                1,
                                ">Toluca",
                                66.0,
                                1,
                                66.0,
                                TypeCharge.GROUP,
                                TypeOperation.PLUS
                            )
                        )
                        insertCharge(
                            ChargeDb(
                                2,
                                ">Marquesa",
                                98.0,
                                1,
                                98.0,
                                TypeCharge.GROUP,
                                TypeOperation.PLUS
                            )
                        )
                        insertCharge(
                            ChargeDb(
                                3,
                                ">Santa fe",
                                82.0,
                                1,
                                82.0,
                                TypeCharge.GROUP,
                                TypeOperation.PLUS
                            )
                        )
                        insertCharge(
                            ChargeDb(
                                4,
                                ">Periferico",
                                10.0,
                                1,
                                10.0,
                                TypeCharge.GROUP,
                                TypeOperation.PLUS
                            )
                        )

                        insertCharge(
                            ChargeDb(
                                5,
                                "<Periferico",
                                31.0,
                                1,
                                31.0,
                                TypeCharge.GROUP,
                                TypeOperation.PLUS
                            )
                        )
                        insertCharge(
                            ChargeDb(
                                6,
                                "<Santa fe",
                                82.0,
                                1,
                                82.0,
                                TypeCharge.GROUP,
                                TypeOperation.PLUS
                            )
                        )
                        insertCharge(
                            ChargeDb(
                                7,
                                "<Marquesa",
                                98.0,
                                1,
                                98.0,
                                TypeCharge.GROUP,
                                TypeOperation.PLUS
                            )
                        )
                        insertCharge(
                            ChargeDb(
                                8,
                                "<Toluca",
                                66.0,
                                1,
                                66.0,
                                TypeCharge.GROUP,
                                TypeOperation.PLUS
                            )
                        )

                        insertCharge(
                            ChargeDb(
                                9,
                                "Gasolina",
                                23.0,
                                1,
                                345.0,
                                TypeCharge.GROUP,
                                TypeOperation.PLUS
                            )
                        )
                        insertCharge(
                            ChargeDb(
                                11,
                                "(E)",
                                12.0,
                                1,
                                12.0,
                                TypeCharge.GROUP,
                                TypeOperation.PLUS
                            )
                        )
                        insertCharge(
                            ChargeDb(
                                12,
                                "Extra Group",
                                0.0,
                                1,
                                0.0,
                                TypeCharge.GROUP,
                                TypeOperation.PLUS
                            )
                        )
                        insertCharge(
                            ChargeDb(
                                15,
                                "Minus Group",
                                0.0,
                                1,
                                0.0,
                                TypeCharge.GROUP,
                                TypeOperation.MINUS
                            )
                        )

                        insertCharge(
                            ChargeDb(
                                10,
                                "Gasolina/2",
                                23.0,
                                8,
                                200.0,
                                TypeCharge.PERSONAL,
                                TypeOperation.MINUS
                            )
                        )
                        insertCharge(
                            ChargeDb(
                                13,
                                "Extras",
                                0.0,
                                1,
                                0.0,
                                TypeCharge.PERSONAL,
                                TypeOperation.PLUS
                            )
                        )
                        insertCharge(
                            ChargeDb(
                                14,
                                "Deducciones",
                                0.0,
                                1,
                                0.0,
                                TypeCharge.PERSONAL,
                                TypeOperation.MINUS
                            )
                        )
                        insertCharge(
                            ChargeDb(
                                16,
                                "Viaje a casa",
                                0.0,
                                1,
                                0.0,
                                TypeCharge.PERSONAL,
                                TypeOperation.PLUS
                            )
                        )

                        insertPerson(PersonDb(1, "Charly", "7228507896"))
                        insertPerson(PersonDb(2, "Maik", "7223731006"))
                        insertPerson(PersonDb(3, "Orihuela", "7224124088"))
                        insertPerson(PersonDb(4, "Mayle", "7226455089"))
                        insertPerson(PersonDb(5, "Nestor", "7228593599"))

                    }
                }
            }
        }

    }
}