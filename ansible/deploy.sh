#!/bin/sh

target_environment=$1
build_yml=$2
echo "Setting up $build_yml on $target_environment"
shift
shift

# create VM(s)
cd $target_environment
vagrant up
cd ..

# build and copy artifacts to roles/../files
./build.sh

# prepare insecure ssh
rm -f /tmp/vagrant.key 
curl --silent https://raw.github.com/mitchellh/vagrant/master/keys/vagrant >/tmp/vagrant.key
chmod 400 /tmp/vagrant.key 
export ANSIBLE_HOST_KEY_CHECKING=False

echo "For ´production': COUCHDB_PASSWORD=$COUCHDB_PASSWORD"

# provision
ansible-playbook $build_yml.yml --user=vagrant --sudo --timeout=100 --inventory-file=$target_environment/inventory --private-key=/tmp/vagrant.key --module-path modules $*
