attribute vec4 vPosition;
attribute vec2 givenTexPosition;

uniform mat4 uMVPMatrix;
varying vec2 texPosition;

void main() {
  texPosition = givenTexPosition;
  gl_Position = uMVPMatrix * vPosition;
}