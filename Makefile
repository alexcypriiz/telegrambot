prebuild:
	ansible-vault decrypt --vault-password-file ${PWD}/../passkey \
				  ${PWD}/src/main/resources/application.yaml

build:
	docker build -t bot .
	ansible-vault encrypt --vault-password-file ${PWD}/../passkey \
				  ${PWD}/src/main/resources/application.yaml

run:
	docker run -ti -v ${PWD}/../telegramdb/:/opt/data bot