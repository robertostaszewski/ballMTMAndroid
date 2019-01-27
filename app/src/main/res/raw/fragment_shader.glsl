precision mediump float;

uniform sampler2D diffuseTexture;

varying vec2 texPosition;

void main() {
  gl_FragColor = texture2D(diffuseTexture, texPosition);
}