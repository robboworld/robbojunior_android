#!/bin/sh
cd ..
npm run build
cp ./src/build/bundles/app.bundle.js  ./android/RobboJunior/app/src/main/assets/HTML5
cp ./src/build/bundles/app.bundle.js.map  ./android/RobboJunior/app/src/main/assets/HTML5
