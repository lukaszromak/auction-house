const prod = {
    url: {
      API_BASE_URL: '20.23.27.146/api'
    }
  }
  
  const dev = {
    url: {
      API_BASE_URL: 'http://localhost:8080/api'
    }
  }
  
  export const config = process.env.NODE_ENV === 'development' ? dev : prod