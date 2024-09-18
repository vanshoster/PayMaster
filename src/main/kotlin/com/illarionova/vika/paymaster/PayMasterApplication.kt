package com.illarionova.vika.paymaster

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.illarionova.vika.paymaster"])
class PayMasterApplication

fun main(args: Array<String>) {
	runApplication<PayMasterApplication>(*args)
}
