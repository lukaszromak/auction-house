const prod = {
    url: {
        IMAGE_BASE_URL: 'https://lrauctionhouse.blob.core.windows.net/auctionhouse/'
    }
  }
  
  const dev = {
    url: {
        IMAGE_BASE_URL: 'http://localhost:8080/images/'
    }
  }
  
  export const config = process.env.NODE_ENV === 'development' ? dev : prod