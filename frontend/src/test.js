

var permute = function(nums) {
    const backtrack = (res, tmp, nums) => {
        if (tmp.length === nums.length) {
            res.push(tmp.slice(0));
            return ;
        }
        
        /**
         * 因为是distinct，所以可以每次都从0遍历整个数组,
         * 只要是出现在tmp中就跳过。
         */
        for (let i = 0; i < nums.length; i++) {
            if (tmp.indexOf(nums[i]) !== -1) {
                continue;   // skip used
            }
            tmp.push(nums[i]);
            backtrack(res, tmp, nums);
            tmp.pop();
        }
    }
    
    let res = [];
    backtrack(res, [], nums);
    return res;
};