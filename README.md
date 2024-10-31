# Bazel Testcontainer Example

This sample repo demonstrates how to integrate [Bazel](https://bazel.build/) and [Testcontainers](https://testcontainers.com/).

You may be tempted to do `docker pull` either in a `sh_test` prior or even outside of `bazel` itself, but this is not guaranteed to work
in a remote execution environment.

Instead this repo uses [rules_oci](https://github.com/bazel-contrib/rules_oci) to create a OCI image which is passed as the _data_ dependency for a
particular `java_test`.

The test itself takes care of loading the image into the current Docker environment before using it in a `GenericContainer`.
