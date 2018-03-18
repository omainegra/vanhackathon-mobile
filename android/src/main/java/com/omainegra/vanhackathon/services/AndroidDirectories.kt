package com.omainegra.vanhackathon.services

import android.content.Context
import java.io.File

/**
 * Created by omainegra on 3/14/18.
 */

class AndroidDirectories(private val context: Context) : Directories {
    override fun cache(): File = context.cacheDir
}