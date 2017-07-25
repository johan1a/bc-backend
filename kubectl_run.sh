#!/bin/sh
kubectl run bc-backend --image=docker.io/johan1a/bc-backend:latest --port=8080
