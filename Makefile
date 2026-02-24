cr_cmd=podman  # docker
cr_extras=--entrypoint ""  # -u $(id -u):$(id -g)
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

.PHONY: image-build
image-build:
	$(cr_cmd) build -f Containerfile -t $(cr_image) .

.PHONY: start-gradle-container
start-gradle-container:
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
	rm -rf app/build
	rm -rf .gradle
