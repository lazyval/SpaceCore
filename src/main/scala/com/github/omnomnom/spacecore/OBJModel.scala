package com.github.omnomnom.spacecore

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 */

class OBJModel(path: String) extends NewModel() with OBJParser {
  loadModel(path)
}
