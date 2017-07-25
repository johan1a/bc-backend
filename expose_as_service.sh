#!/bin/bash
kubectl expose deployment/bc-backend --type="NodePort" --port 3000
