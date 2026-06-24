package org.example.project.data.security

import org.example.project.domain.service.HashService
import org.kotlincrypto.hash.sha2.SHA256

class HashServiceImpl : HashService {
    @OptIn(ExperimentalStdlibApi::class)
    override fun hash(input: String): String {
        return SHA256().digest(input.encodeToByteArray()).toHexString()
    }
}
