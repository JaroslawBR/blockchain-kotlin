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
            var magickNumb = 0
            var newBlock: Block
            val startTime = System.currentTimeMillis()
            val timestamp = System.currentTimeMillis()

            while (true) {
                val sha256 = applySha256("$i$timestamp${block.lastOrNull()?.hash ?: 0}$magickNumb")
                if (sha256.substring(0 until zeroes) == "0".repeat(zeroes)) {
                    val endTime: Int = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                    newBlock = Block(block.size + 1, timestamp, sha256, endTime, magickNumb)
                    break
                }
                magickNumb ++
            }
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