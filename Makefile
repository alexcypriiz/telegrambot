ARCH := $(uname -m)

prebuild:
	ansible-vault decrypt --vault-password-file ${PWD}/../passkey \
				  ${PWD}/src/main/resources/application.yaml

build:
ifeq ($(ARCH),armv7l)
	docker build -t bot -f Dockerfile_arm .
else
	docker build -t bot -f Dockerfile .
endif
	ansible-vault encrypt --vault-password-file ${PWD}/../passkey \
				  ${PWD}/src/main/resources/application.yaml

run:
	docker run -ti -v ${PWD}/../telegramdb/:/opt/data bot