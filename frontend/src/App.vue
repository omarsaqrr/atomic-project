<script setup>

import { onMounted, ref } from 'vue'

import { fetchConsumedMessages, fetchInfos, publishMessage } from './api/infos'



const infos = ref(null)

const messages = ref([])

const loading = ref(true)

const error = ref('')

const publishPayload = ref('{"event": "demo"}')

const publishing = ref(false)

const publishError = ref('')

const publishSuccess = ref('')



async function loadData() {

  loading.value = true

  error.value = ''



  try {

    const [infosData, messagesData] = await Promise.all([

      fetchInfos(),

      fetchConsumedMessages()

    ])

    infos.value = infosData

    messages.value = messagesData

  } catch (err) {

    error.value = err.message

  } finally {

    loading.value = false

  }

}



async function handlePublish() {

  publishing.value = true

  publishError.value = ''

  publishSuccess.value = ''



  try {

    const payload = JSON.parse(publishPayload.value)

    const result = await publishMessage(payload)

    publishSuccess.value = result?.location

      ? `Message published (id: ${result.location})`

      : 'Message published'

    await loadData()

  } catch (err) {

    publishError.value = err instanceof SyntaxError

      ? 'Invalid JSON payload'

      : err.message

  } finally {

    publishing.value = false

  }

}



onMounted(loadData)

</script>



<template>

  <main class="page">

    <header class="hero">

      <p class="eyebrow">Interview Exercise</p>

      <h1>Kafka Infos Dashboard</h1>

      <p class="subtitle">

        Vue 3 frontend calling the Spring Boot <code>/api/infos</code> and

        <code>/api/messages</code> endpoints backed by Kafka.

      </p>

      <button class="refresh" type="button" @click="loadData">Refresh</button>

    </header>



    <section v-if="loading" class="card">Loading backend data...</section>

    <section v-else-if="error" class="card error">{{ error }}</section>



    <template v-else>

      <section class="card">

        <h2>Cluster Overview</h2>

        <dl class="grid">

          <div>

            <dt>Broker</dt>

            <dd>{{ infos.brokerVersion }}</dd>

          </div>

          <div>

            <dt>Cluster ID</dt>

            <dd>{{ infos.clusterId }}</dd>

          </div>

          <div>

            <dt>Fetched At</dt>

            <dd>{{ infos.fetchedAt }}</dd>

          </div>

        </dl>

      </section>



      <section class="card">

        <h2>Kafka Topics</h2>

        <table v-if="infos.topics.length">

          <thead>

            <tr>

              <th>Name</th>

              <th>Partitions</th>

              <th>Replication Factor</th>

            </tr>

          </thead>

          <tbody>

            <tr v-for="topic in infos.topics" :key="topic.name">

              <td>{{ topic.name }}</td>

              <td>{{ topic.partitions }}</td>

              <td>{{ topic.replicationFactor }}</td>

            </tr>

          </tbody>

        </table>

        <p v-else>No topics found.</p>

      </section>



      <section class="card">

        <h2>Publish Message</h2>

        <p class="hint">Send JSON to <code>POST /api/messages</code> to produce a Kafka event.</p>

        <textarea

          v-model="publishPayload"

          class="payload-input"

          rows="4"

          spellcheck="false"

        />

        <div class="publish-actions">

          <button

            class="refresh"

            type="button"

            :disabled="publishing"

            @click="handlePublish"

          >

            {{ publishing ? 'Publishing...' : 'Publish' }}

          </button>

        </div>

        <p v-if="publishError" class="error">{{ publishError }}</p>

        <p v-else-if="publishSuccess" class="success">{{ publishSuccess }}</p>

      </section>



      <section class="card">

        <h2>Consumed Messages (saved to MySQL)</h2>

        <table v-if="messages.length">

          <thead>

            <tr>

              <th>Topic</th>

              <th>Partition</th>

              <th>Offset</th>

              <th>Payload</th>

              <th>Consumed At</th>

            </tr>

          </thead>

          <tbody>

            <tr v-for="message in messages" :key="message.id">

              <td>{{ message.topic }}</td>

              <td>{{ message.partition }}</td>

              <td>{{ message.offset }}</td>

              <td><code>{{ message.payload }}</code></td>

              <td>{{ message.consumedAt }}</td>

            </tr>

          </tbody>

        </table>

        <p v-else>No consumed messages yet. Publish one above.</p>

      </section>

    </template>

  </main>

</template>


