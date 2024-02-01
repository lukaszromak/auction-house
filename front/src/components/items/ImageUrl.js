const prod = {
    url: {
        IMAGE_BASE_URL: 'http://98.64.67.168/api'
    }
  }
  
  const dev = {
    url: {
        IMAGE_BASE_URL: 'https://lrauctionhouse.blob.core.windows.net/auctionhouse/'
    }
  }
  
  export const config = process.env.NODE_ENV === 'development' ? dev : prod