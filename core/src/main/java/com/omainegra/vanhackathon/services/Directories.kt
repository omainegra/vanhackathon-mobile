package com.omainegra.vanhackathon.services

import java.io.File

/**
 * Created by omainegra on 3/14/18.
 */

interface Directories {
    fun cache(): File
}

class DirectoriesNoOp : Directories {
    override fun cache(): File = File("/some/path/to/cache")
}