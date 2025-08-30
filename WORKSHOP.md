JVM Troubleshooting Workshop
============================


1. Setup up a JVM
------------------

Set up an environment an JVM that is similar to your production. You can run locally but it is preferrable to have a Linux environment

Options include:

- VM
- Docker
- Kubernetes

You can use `run_locally.sh` and `run_with_docker.sh` as a starting point.

Set up a development loop to easily deploy local changes.

2. Come up with JVM Options you Deem Reasonable
------------------------------------------------

Come up with JVM options you deem reasonable for Spring Pet Clinic deployed on a small scale.

You can stick with defaults if you think ergonomics result in an acceptable result.

3. Add Flight Recorder Events at Critical Points in the Applicaiton
--------------------------------------------------------------------

Add Flight Recorder events at critical points in the applicaiton that allow you to profile the application.

If you prefer different profiling technologies feel free to use them.

## Hints

Good starting locations to generate JFR events are:

- HTTP requests
- Controller methods
- Database access
- Application context initialization

## Further Hints

- Check the Web, some frameworks may already offer support for JFR.
- Make sure you have access to logs.


