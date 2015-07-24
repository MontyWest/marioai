package com.montywest.marioai.rules

sealed trait KeyPress;

case object KeyLeft extends KeyPress;
case object KeyRight extends KeyPress;
case object KeyJump extends KeyPress;
case object KeySpeed extends KeyPress;