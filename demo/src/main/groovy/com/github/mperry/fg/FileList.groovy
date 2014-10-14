package com.github.mperry.fg

import fj.data.IO
import groovy.transform.TypeChecked

/**
 * Created by mperry on 4/08/2014.
 */
@TypeChecked
class FileList {

    IO<List<File>> ls(File f) {
        { -> f.listFiles() as List } as IO
    }

    IO<Long> size(File f) {
        { -> f.length() } as IO
    }



}
