#!/bin/bash
kubectl get pods -o jsonpath="{..image}" |tr -s '[[:space:]]' '\n' |sort |uniq -c
