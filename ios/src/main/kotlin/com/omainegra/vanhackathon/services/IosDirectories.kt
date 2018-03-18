package com.omainegra.vanhackathon.services

import org.robovm.apple.foundation.NSPathUtilities
import java.io.File

class IosDirectories : Directories {
    override fun cache(): File = File(NSPathUtilities.getTemporaryDirectory())
}