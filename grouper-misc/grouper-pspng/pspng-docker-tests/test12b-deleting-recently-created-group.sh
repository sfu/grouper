#!/bin/bash
#
# description: test12a - Making sure provisioned groups are deleted when deleted from grouper
# configs: ldap-attributes, posix-groups, goun-groups, posix-groups-bushy, posix-groups-bushy-dnsearch
#
# This test takes verifies the correct incremental deprovisioning is happening when
# groups are created
#

set -o errexit
set -o pipefail
set -o nounset

ME=$(basename "$0")

# Functions for test harness: read_test_config_files, test_start, start_docker, etc
# (This also pulls in functions.bash (log_always, temp-file functions, etc)
. "$(dirname "${BASH_SOURCE[0]}")/functions-pspng-testing.bash"


###
# Pull-in shell functions for generating config and verifying provisioning
# These are usually specific for different provisioning endpoints
# And the same 
#   Config Building: output_log4j_properties, output_grouper_loader_properties
#   Hooks: test_is_starting, test_is_ending
#   Provisioning verification: validate_provisioning <group> <correct members (comma-separated, alphabetical)>
# Defines: $flavor
read_test_config_files "${1:-posix-groups}"


# Note that the test is starting, saves start-time, etc
test_start "$ME" "Pspng ($flavor): provisioning groups and membership changes"

################
## CONFIGURE GROUPER
## This will populate and define GROUPER_CONFIG_DIR

create_grouper_daemon_config

run_after_parent_test test02a-basic-provisioning.sh

################
## START DOCKER

start_docker "${ME}_$flavor" 

init_test_scenario_variables
wait_for_grouper_daemon_to_be_running


# We're going to create a group3 and then quickly delete it because we've seen
# pspng ignore group-deletes 

# Create a group based on group2 name
GROUP3_NAME=$(sed 's/2/3/g' <<<"$GROUP2_NAME")
run_in_grouper_daemon create-group --group_name "$GROUP3_NAME"
run_in_grouper_daemon add-member --group "$GROUP3_NAME" banderson agasper bbrown705
await_changelog_catchup

validate_provisioning "$GROUP3_NAME" "agasper,banderson,bbrown705"

run_in_grouper_daemon delete-group --group_name "$GROUP2_NAME"
run_in_grouper_daemon delete-group --group_name "$GROUP3_NAME"
await_changelog_catchup

validate_deprovisioning "$GROUP2_NAME"
validate_deprovisioning "$GROUP3_NAME"

#make sure extra groups were not provisioned
validate_deprovisioning "$UNPROVISIONED_GROUP_NAME"
wrap_up
assert_empty "$ERRORS" "Check for exceptions in grouper_error.log"
test_success
