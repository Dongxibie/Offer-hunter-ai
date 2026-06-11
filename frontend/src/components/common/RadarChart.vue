<template>
  <div ref="chartRef" :style="{ width: '100%', height: '400px' }"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import * as echarts from 'echarts'

const props = defineProps<{
  data: Record<string, number>
}>()

const chartRef = ref<HTMLElement>()
let chart: echarts.ECharts | null = null

function renderChart() {
  if (!chartRef.value || !props.data) return

  if (!chart) {
    chart = echarts.init(chartRef.value)
  }

  const labels = Object.keys(props.data)
  const values = Object.values(props.data)

  chart.setOption({
    radar: {
      indicator: labels.map((label) => ({
        name: label,
        max: 100,
      })),
      shape: 'circle',
      splitNumber: 5,
      axisName: {
        color: '#333',
        fontSize: 14,
      },
      splitLine: {
        lineStyle: {
          color: '#e0e0e0',
        },
      },
      splitArea: {
        show: true,
        areaStyle: {
          color: ['rgba(26, 115, 232, 0.05)', 'rgba(26, 115, 232, 0.1)'],
        },
      },
    },
    series: [
      {
        type: 'radar',
        data: [
          {
            value: values,
            name: '能力评分',
            areaStyle: {
              color: 'rgba(26, 115, 232, 0.2)',
            },
            lineStyle: {
              color: '#1a73e8',
              width: 2,
            },
            itemStyle: {
              color: '#1a73e8',
            },
          },
        ],
      },
    ],
  })
}

function handleResize() {
  chart?.resize()
}

onMounted(() => {
  renderChart()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  chart?.dispose()
  chart = null
})

watch(() => props.data, () => {
  renderChart()
}, { deep: true })
</script>
