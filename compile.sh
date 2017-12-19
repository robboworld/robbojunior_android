#!/bin/sh

npm run build

cp ./src/build/bundles/app.bundle.js ./android/ScratchJr/app/src/main/assets/HTML5
cp ./src/build/bundles/app.bundle.js.map ./android/ScratchJr/app/src/main/assets/HTML5
