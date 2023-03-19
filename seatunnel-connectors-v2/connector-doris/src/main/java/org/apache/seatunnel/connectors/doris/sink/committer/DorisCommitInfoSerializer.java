/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.seatunnel.connectors.doris.sink.committer;

import org.apache.seatunnel.api.serialization.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/** define how to serialize DorisCommittable. */
public class DorisCommitInfoSerializer implements Serializer<DorisCommitInfo> {

    @Override
    public byte[] serialize(DorisCommitInfo dorisCommittable) throws IOException {
        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final DataOutputStream out = new DataOutputStream(baos)) {
            out.writeUTF(dorisCommittable.getHostPort());
            out.writeUTF(dorisCommittable.getDb());
            out.writeLong(dorisCommittable.getTxbID());

            out.flush();
            return baos.toByteArray();
        }
    }

    @Override
    public DorisCommitInfo deserialize(byte[] serialized) throws IOException {
        try (final ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
                final DataInputStream in = new DataInputStream(bais)) {
            final String hostPort = in.readUTF();
            final String db = in.readUTF();
            final long txnId = in.readLong();
            return new DorisCommitInfo(hostPort, db, txnId);
        }
    }
}