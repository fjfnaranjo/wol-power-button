cr_cmd=podman  # docker
cr_extras=# -u $(id -u):$(id -g)
cr_image=wolpowerbutton:latest
cr_name=wol-power-button-gradle
cr_workspace=-w "/workspace"
cr_gradle_cache=-v "$$(pwd)/.gradle/root-android:/root/.android"
cr_android_cache=-v "$$(pwd)/.gradle/root-gradle:/root/.gradle"
cr_volumes=-v "$$(pwd):/workspace" $(cr_workspace) $(cr_gradle_cache) $(cr_android_cache)
cr_exex=$(cr_cmd) exec -ti $(cr_workspace) $(cr_name)

.PHONY: all
all:
	# No deafult targets.
	# Try: image-build debug-build debug-install debug-run

.PHONY: generate-icons
generate-icons:
	mkdir -p app/src/main/res/mipmap-ldpi
	mkdir -p app/src/main/res/mipmap-mdpi
	mkdir -p app/src/main/res/mipmap-hdpi
	mkdir -p app/src/main/res/mipmap-xhdpi
	mkdir -p app/src/main/res/mipmap-xxhdpi
	mkdir -p app/src/main/res/mipmap-xxxhdpi
	$(cr_exex) rsvg-convert -w  36 -h  36 -o app/src/main/res/mipmap-ldpi/ic_launcher.png app/src/main/res/drawable/ic_launcher.xml
	$(cr_exex) rsvg-convert -w  48 -h  48 -o app/src/main/res/mipmap-mdpi/ic_launcher.png app/src/main/res/drawable/ic_launcher.xml
	$(cr_exex) rsvg-convert -w  72 -h  72 -o app/src/main/res/mipmap-hdpi/ic_launcher.png app/src/main/res/drawable/ic_launcher.xml
	$(cr_exex) rsvg-convert -w  96 -h  96 -o app/src/main/res/mipmap-xhdpi/ic_launcher.png app/src/main/res/drawable/ic_launcher.xml
	$(cr_exex) rsvg-convert -w 114 -h 114 -o app/src/main/res/mipmap-xxhdpi/ic_launcher.png app/src/main/res/drawable/ic_launcher.xml
	$(cr_exex) rsvg-convert -w 192 -h 192 -o app/src/main/res/mipmap-xxxhdpi/ic_launcher.png app/src/main/res/drawable/ic_launcher.xml

.PHONY: image-build
image-build:
	$(cr_cmd) build -f Containerfile -t $(cr_image) .

.PHONY: start-gradle-container
start-gradle-container:
	mkdir -p .gradle/root-android
	mkdir -p .gradle/root-gradle
	$(cr_cmd) run --rm -d --name $(cr_name) $(cr_workspace) $(cr_volumes) $(cr_extras) $(cr_image) sleep infinity

.PHONY: stop-gradle-container
stop-gradle-container:
	$(cr_cmd) kill $(cr_name)

.PHONY: debug-build
debug-build:
	$(cr_exex) gradle assembleDebug

.PHONY: debug-install
debug-install:
	adb install -r app/build/outputs/apk/debug/app-debug.apk

.PHONY: debug-run
debug-run:
	adb shell am start -n com.example.wolpowerbutton/.MainActivity -W

.PHONY: debug-logcat
debug-logcat:
	adb logcat | grep com.example.wolpowerbutton

.PHONY: clean
clean:
	rm -rf app/build/outputs/apk/debug

.PHONY: clean-all
clean-all: clean
	rm -rf app/src/main/res/mipmap-ldpi
	rm -rf app/src/main/res/mipmap-mdpi
	rm -rf app/src/main/res/mipmap-hdpi
	rm -rf app/src/main/res/mipmap-xhdpi
	rm -rf app/src/main/res/mipmap-xxhdpi
	rm -rf app/src/main/res/mipmap-xxxhdpi
	rm -rf app/build
	rm -rf .gradle
