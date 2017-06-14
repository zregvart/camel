#!/usr/bin/env groovy
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

properties([buildDiscarder(logRotator(artifactNumToKeepStr: '5', numToKeepStr: env.BRANCH_NAME=='master'?'10':'5'))])

def MAIL_TO     = 'zoran@regvart.com'
def JAVA_8      = 'JDK 1.8 (latest)'
def JAVA_9      = 'JDK 9 b156 (early access build) with project Jigsaw'
def JAVA_9_OPTS = '''\
--add-modules java.activation
--add-exports=java.xml/com.sun.org.apache.xml.internal.resolver.tools=ALL-UNNAMED
--add-exports=java.xml/com.sun.org.apache.xml.internal.resolver=ALL-UNNAMED
--add-opens java.base/java.text=ALL-UNNAMED
--add-opens java.desktop/java.awt.font=ALL-UNNAMED
--add-opens java.base/java.util=ALL-UNNAMED
--add-opens java.base/java.lang=ALL-UNNAMED
--add-opens java.base/java.lang.reflect=ALL-UNNAMED
--add-opens java.base/java.util.regex=ALL-UNNAMED
--add-opens java.base/java.net=ALL-UNNAMED
'''

def maven(java_name, goals, opts, maven_opts='') {
    def java_home = tool name: java_name

    withEnv(["JAVA_HOME=$java_home", "MAVEN_OPTS=$maven_opts"]) {
        def LOCAL_REPO  = '/home/jenkins/jenkins-slave/maven-repositories/0'

        echo "JAVA_HOME=$java_home"
        echo "MAVEN_OPTS=$maven_opts"

        sh "./mvnw $goals -B -e -fae -V -Dmaven.repo.local=$LOCAL_REPO $opts"
        echo pwd()
    }
}

try {
timestamps {

node('ubuntu') {
    stage('Checkout') {
        checkout scm
    }

    stage('Checks') {
        try {
            maven(JAVA_8, 'checkstyle:check', '-Psourcecheck')
        } catch(e){} finally {
            checkstyle pattern: '**/checkstyle-result.xml'
        }
    }

    stage('Build') {
        // build on Java 8 and Java 9 in parallel
        parallel 'Java 8': {

            maven(JAVA_8, 'clean install', '-Dnoassembly -Dmaven.test.skip.exec=true -Dmaven.install.skip=true')
        }, 'Java 9': {
            node('ubuntu') {
                checkout scm

                maven(JAVA_9, 'clean install', '-Dnoassembly -Dmaven.test.skip.exec=true -Dmaven.install.skip=true', JAVA_9_OPTS)
            }
        }
    }

    stage('Test') {
        // test only on Java 8
        maven(JAVA_8, 'test', '-Pintegration -Dnoassembly')
    }

    stage('Deploy') {
        // deploy Java 8 built artifacts
        maven(JAVA_8, 'install', '-Pdeploy -Dnoassembly -Dmaven.test.skip.exec=true')
    }

} // node

} // timestamps
} finally {
    node('ubuntu') {
        emailext body: "Jenkins has built: ${env.JOB_NAME}\n\nStatus: ${currentBuild.result}\n\nSee: ${env.BUILD_URL}", recipientProviders: [[$class: 'CulpritsRecipientProvider'], [$class: 'FailingTestSuspectsRecipientProvider'], [$class: 'FirstFailingBuildSuspectsRecipientProvider']], replyTo: MAIL_TO, subject: "${env.JOB_NAME} - build ${env.BUILD_DISPLAY_NAME} - ${currentBuild.result}", to: MAIL_TO
    }
}

