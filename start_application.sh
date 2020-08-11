#!/bin/bash

set_current_dir() {

  SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null && pwd)"
}

start_functional_environment() {

  echo "Starting functional environment..."
  cd "${SCRIPT_DIR}" || exit

}


build_application_jar_file() {

  echo "Building the application .jar file..."
  mvn clean install
  echo "Application jar file built successfully"

}

start_application() {

  echo -e "\xE2\x9C\x94 Application Starting !"
  cd "${SCRIPT_DIR}/image-manager-service" || exit
  mvn spring-boot:run

}

set_current_dir
start_functional_environment
build_application_jar_file
start_application
