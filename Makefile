prebuild:
	ansible-vault decrypt --vault-password-file ${PWD}/../passkey \
				  ${PWD}/src/main/resources/application.yaml

build:
	docker build -t bot .

run:
	docker run -ti -v ${PWD}/../telegramdb/:/opt/data bot bash