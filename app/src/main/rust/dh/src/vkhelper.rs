use jni::{ JNIEnv, objects::{ JClass, JString }, sys::jstring };
use jni_fn::jni_fn;
use vulkano::instance::{Instance, InstanceCreateInfo};
use vulkano::VulkanLibrary;

#[jni_fn("top.kmiit.debughelper")]
pub fn getVulkanVersionNative (
    env: JNIEnv,
    _class: JClass,
    _name: JString
) -> jstring {
    let version_str = match get_physical_device_version() {
        Ok(v) => v,
        Err(_) => "unknown".to_string(),
    };

    env.new_string(version_str)
        .expect("Couldn't create java string!")
        .into_raw()
}

fn get_physical_device_version() -> Result<String, Box<dyn std::error::Error>> {
    let library = VulkanLibrary::new()?;
    let instance = Instance::new(library, InstanceCreateInfo::default())?;

    // Get the first physical device
    let physical = instance
        .enumerate_physical_devices()?
        .next()
        .ok_or("No physical device found")?;

    let version = physical.properties().api_version;

    // Return formatted version: Major.Minor.Patch
    Ok(format!("{}.{}.{}", version.major, version.minor, version.patch))
}
