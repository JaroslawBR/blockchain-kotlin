package blockchain

import java.security.MessageDigest

fun main() {
    Blockchain().start()
}

class Blockchain {

    data class Block(val id: Int, val timestamp: Long, val hash: String, val time: Int, val magicNumber: Int)
    private val block = mutableListOf<Block>()

    private fun applySha256(input: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            /* Applies sha256 to our input */
            val hash = digest.digest(input.toByteArray(charset("UTF-8")))
            val hexString = StringBuilder()
            for (elem in hash) {
                val hex = Integer.toHexString(0xff and elem.toInt())
                if (hex.length == 1) hexString.append('0')
                hexString.append(hex)
            }
            hexString.toString()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun crate(){
        print("Enter how many zeros the hash must start with: ")
        val zeroes = readln().toInt()
        val blockNum = 5

        for (i in 1..blockNum) {
            var magicNumb = 0
            val startTime = System.currentTimeMillis()
            val timestamp = System.currentTimeMillis()
            val lastBlockHash = block.lastOrNull()?.hash ?: "0"
            val conditionPrefix = "0".repeat(zeroes)

            while (!applySha256("$i$timestamp$lastBlockHash$magicNumb").startsWith(conditionPrefix)) {
                magicNumb++
            }

            val endTime = ((System.currentTimeMillis() - startTime) / 1000).toInt()
            val newBlock = Block(block.size + 1, timestamp, applySha256("$i$timestamp$lastBlockHash$magicNumb"), endTime, magicNumb)
            block.add(newBlock)
        }

    }

    private fun show(){
        for (i in block) {
            val previousHash = if (block.indexOf(i) != 0)  block[block.indexOf(i) - 1].hash else "0"

            println("Block:")
            println("Id: ${i.id}")
            println("Timestamp: ${i.timestamp}")
            println("Magic number: ${i.magicNumber}")
            println("Hash of the previous block:")
            println(previousHash)
            println("Hash of the block:")
            println(i.hash)
            println("Block was generating for ${i.time} seconds\n")
        }
    }

    fun start(){
        crate()
        show()
    }






}